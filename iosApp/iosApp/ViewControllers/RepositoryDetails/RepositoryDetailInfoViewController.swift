//
//  RepositoryDetailInfoViewController.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 23.06.2022.
//

import UIKit
import SafariServices
import MaterialComponents.MaterialActivityIndicator
import shared
import SwiftyMarkdown


class RepositoryDetailInfoViewController: UIViewController {
    @IBOutlet private weak var linkLabel: UILabel!
    
    @IBOutlet private weak var licenseLabelWithValue: UILabel!
    @IBOutlet private weak var watchersLabelWithValue: UILabel!
    @IBOutlet private weak var forksLabelWithValue: UILabel!
    @IBOutlet private weak var starsLabelWithValue: UILabel!
    
    @IBOutlet private weak var licenseLabel: UILabel!
    @IBOutlet private weak var starsLabel: UILabel!
    @IBOutlet private weak var watchersLabel: UILabel!
    @IBOutlet private weak var forksLabel: UILabel!
    @IBOutlet private weak var readmeLabel: UILabel!
    @IBOutlet private weak var contentStackView: UIStackView!
    
    @IBOutlet private weak var errorView: ErrorView!
    @IBOutlet private weak var readmeErrorView: ErrorView!
    @IBOutlet private weak var reloadButton: UIButton!
    @IBOutlet private weak var readmeReloadButton: UIButton!
    
    @IBOutlet private weak var activityIndicator: MDCActivityIndicator!
    @IBOutlet private weak var readmeActivityIndicator: MDCActivityIndicator!
    
    private var repo: Repo
    private let appRepo = DI.shared.appRepo
    
    init(repo: Repo, nibName: String?, bundle: Bundle?) {
        self.repo = repo
        super.init(nibName: nibName, bundle: bundle)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUI()
        getRepoInfo()
        getReadmeAndUpdateUI()
    }
    
    private func getRepoInfo() {
        loadingStart(content: contentStackView, errorView: errorView, reloadButton: reloadButton, indicator: activityIndicator)
        appRepo.getRepository(ownerName: repo.owner, repoName: repo.name) { [weak self] repo, error in
            self?.activityIndicator.hide()
            if let error = error {
                self?.reloadButton.isHidden = false
                self?.showErrorView(self?.errorView, for: error.asCustomError())
                return
            }
            if let repo = repo {
                self?.repo = repo
                self?.updateRepoInfoUI()
            }
            self?.contentStackView.isHidden = false
        }
    }
    
    private func getReadmeAndUpdateUI() {
        loadingStart(content: readmeLabel, errorView: readmeErrorView, reloadButton: readmeReloadButton, indicator: readmeActivityIndicator)
        appRepo.getRepositoryReadme(ownerName: repo.owner, repoName: repo.name, branchName: repo.branch){ [weak self] (readme, error) in
            self?.readmeActivityIndicator.hide()
            if let error = error {
                let customError: CustomError = error.asCustomError()
                if customError is CustomError.NotFound {
                    self?.readmeLabel.text = NSLocalizedString("errorType.readmeNotFound.description", comment: "")
                    self?.readmeLabel.isHidden = false
                    return
                }
                self?.showErrorView(self?.readmeErrorView, for: customError)
                self?.readmeReloadButton.isHidden = false
                return
            }
            self?.readmeLabel.isHidden = false
            if (readme ?? "").isEmpty {
                self?.readmeLabel.text = NSLocalizedString("repoDetails.readmeLabel.emptyReadme.title", comment: "")
                return
            }
            let md = SwiftyMarkdown(string: readme!)
            self?.readmeLabel.attributedText = md.attributedString()
        }
    }
    
    private func updateRepoInfoUI(){
        title = repo.name
        linkLabel.text = repo.htmlUrl
        licenseLabelWithValue.text = repo.license ?? NSLocalizedString("repoDetails.licenseLabelWithValue.noLicense.title", comment: "")
        starsLabelWithValue.text = "\(repo.stars)"
        forksLabelWithValue.text = "\(repo.forks)"
        watchersLabelWithValue.text = "\(repo.watchers)"
    }
    
    private func setUI(){
        setExitButton()
        
        linkLabel.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.linkPressed)))
        reloadButton.setTitle(NSLocalizedString("errorView.reloadButton.retry.title", comment: ""), for: .normal)
        readmeReloadButton.setTitle(NSLocalizedString("errorView.reloadButton.retry.title", comment: ""), for: .normal)
        
        activityIndicator.setColor()
        readmeActivityIndicator.setColor()
        
        licenseLabel.text = NSLocalizedString("repoDetails.licenseLabel.title", comment: "")
        starsLabel.text = NSLocalizedString("repoDetails.starsLabel.title", comment: "")
        forksLabel.text = NSLocalizedString("repoDetails.forksLabel.title", comment: "")
        watchersLabel.text = NSLocalizedString("repoDetails.watchersLabel.title", comment: "")
    }
    
    private func loadingStart(content: UIView, errorView: ErrorView, reloadButton: UIButton, indicator: MDCActivityIndicator) {
        content.isHidden = true
        hideErrorView(errorView)
        reloadButton.isHidden = true
        indicator.show()
    }
    
    @IBAction func reloadButtonTapped(_ sender: UIButton) {
        if sender.accessibilityIdentifier == "reloadButton" {
            getRepoInfo()
            getReadmeAndUpdateUI()
            return
        }
        getReadmeAndUpdateUI()
    }
    
    
    @IBAction private func linkPressed() {
        if let url = URL(string: linkLabel.text!) {
            let safariViewController = SFSafariViewController(url: url)
            present(safariViewController, animated: true,
                    completion: nil)
        }
    }
}

